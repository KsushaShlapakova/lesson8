/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package nsu.ui;

import java.util.Calendar;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Rob Winch
 */
public class Message {

	private Long id;

	@NotEmpty(message = "Name is required.")
	private String name;

	@NotEmpty(message = "Surname is required.")
	private String surname;

    @NotEmpty(message = "Last name is required.")
    private String lastname;

	@NotEmpty(message = "B-day is required.")
	private String bday;

	@NotEmpty(message = "Group is required.")
	private String group;

	private Calendar created = Calendar.getInstance();

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getCreated() {
		return this.created;
	}

	public void setCreated(Calendar created) {
		this.created = created;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

	public String getGroup() { return this.group; }

	public void setGroup(String group) {
		this.group = group;
	}

	public String getBday() {
		return this.bday;
	}

	public void setBday(String bday) { this.bday = bday; }
}
