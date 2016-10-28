package org.nebula.builder.test.pojo;

import java.util.List;
import java.util.Map;

public class Person {

  private String name;
  private int age;
  private Address[] addresses;

  private List<String> emails;

  private Map<String, Person> friends;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public Address[] getAddresses() {
    return addresses;
  }

  public void setAddresses(Address[] addresses) {
    this.addresses = addresses;
  }

  public List<String> getEmails() {
    return emails;
  }

  public void setEmails(List<String> emails) {
    this.emails = emails;
  }

  public Map<String, Person> getFriends() {
    return friends;
  }

  public void setFriends(Map<String, Person> friends) {
    this.friends = friends;
  }
}