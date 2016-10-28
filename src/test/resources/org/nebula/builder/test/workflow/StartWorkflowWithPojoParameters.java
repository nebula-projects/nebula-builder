package org.nebula.builder.test.workflow;

import org.nebula.builder.test.pojo.Person;
import org.nebula.framework.annotation.Start;
import org.nebula.framework.annotation.Workflow;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Workflow
public interface StartWorkflowWithPojoParameters {

  @Start
  public void testStart(Person person, Collection<Person> personCollection, List<Person> personList,
                        Set<Person> personSet, Map<String, Person> personMap, Person[] personArray);

}