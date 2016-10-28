package org.nebula.builder.test.activity;

import org.nebula.builder.test.pojo.Person;

import java.util.List;

@Activity
public interface Activity {

  public void testActivityWithoutReturn();

  public String testActivityWithString(String name);

  public int testActivityWithPrimitiveInt(int number);

  public long testActivityWithPrimitiveLong(long number);

  public float testActivityWithPrimitiveFloat(float number);

  public double testActivityWithPrimitiveDouble(double number);

  public byte testActivityWithPrimitiveByte(byte number);

  public char testActivityWithPrimitiveChar(char number);

  public short testActivityWithPrimitiveShort(short number);

  public boolean testActivityWithPrimitiveBool(boolean number);

  public Person testActivityWithPojo(Person person);

  public List<Person> testActivityWithList(List<Person> persons);

  public long[] testActivityWithArray(String[] names, long[] numbers);


}