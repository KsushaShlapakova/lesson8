/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nsu.ui;

import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dave Syer
 */
public class InMemoryMessageRespository implements MessageRepository {

	private static Connection con = null;
	private static Statement stStudent = null;
	private static Statement stGroup = null;

	public static final String url = "jdbc:mysql://localhost:3306/students?useUnicode=true&characterEncoding=utf8";
	public static final String user = "root";
	public static final String pwd = "";

	private static AtomicLong counter = new AtomicLong();

	private final ConcurrentMap<Long, Message> messages = new ConcurrentHashMap<Long, Message>();

	public InMemoryMessageRespository(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, pwd);
			stStudent = con.createStatement();
			stGroup = con.createStatement();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public Iterable<Message> findAll() throws SQLException {
		ResultSet rsStudents = stStudent.executeQuery("select * from student;");
		while (rsStudents.next()){
			String groupNumber = null;
			ResultSet rsGroups = stGroup.executeQuery("select group_name from `group` where id = '"
														+ rsStudents.getString(2) + "';");
			while(rsGroups.next()){
				groupNumber = rsGroups.getString(1);
			}
			rsGroups.close();
			Message student = new Message();
			student.setId(rsStudents.getLong(1));
			student.setBday(rsStudents.getString(6));
			student.setSurname(rsStudents.getString(4));
			student.setName(rsStudents.getString(3));
			student.setLastname(rsStudents.getString(5));
			student.setGroup(groupNumber);

			messages.putIfAbsent(rsStudents.getLong(1), student);
		}
		return this.messages.values();
	}

	@Override
	public Message save(Message stud) throws SQLException {
		Long id = null;
		boolean groupExistence = checkDB("group", "group_name", String.valueOf(stud.getGroup()));
		boolean studentExistence = checkDB("student", "second_name", stud.getSurname());

		if (!groupExistence) {
			String queryGroup = "insert into `group` (group_name) values ('" + stud.getGroup() + "');";
			stStudent.executeUpdate(queryGroup);
		}

		if (!studentExistence) {
			// Нахожу id группы студента;
			ResultSet rs = stStudent.executeQuery("select id from `group` where group_name = '" + stud.getGroup() + "';");

			String groupID = null;

			while (rs.next()) {
				groupID = rs.getString(1);
			}

			// Добавляю студента в базу;
			String queryStudent = "insert into student (group_id, first_name, second_name, last_name, birthday_date)" +
					" values ('" + groupID + "', '" + stud.getName() + "', '" + stud.getSurname() + "', '" +
					stud.getSurname() + "', '" + stud.getBday() + "');";

			stStudent.executeUpdate(queryStudent);
			ResultSet potencial_id = stStudent.executeQuery("select id from student where second_name = '" + stud.getSurname()+"';");
			while (potencial_id.next()){
				id = potencial_id.getLong(1);
			}
			rs.close();
		}
		stud.setId(id);
		messages.putIfAbsent(id, stud);
		return stud;
	}


	@Override
	public Message findMessage(Long id) {
		return this.messages.get(id);
	}

	public boolean checkDB(String dbName, String field, String value) throws SQLException {
		String query = "SELECT EXISTS(SELECT id FROM `" + dbName +"` WHERE " + field +" = '"+ value + "');";
		ResultSet rs = stStudent.executeQuery(query);

		while (rs.next()){
			if (rs.getString(1).equals("0")){
				rs.close();
				return false;
			}
		}
		return true;
	}
}
