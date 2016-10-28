package org.nebula.builder.test.workflow;

import org.nebula.framework.annotation.Start;
import org.nebula.framework.annotation.Workflow;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Workflow
public interface StartWorkflowWithCollectionParameters {

  @Start
  public void testStart(Collection names, List<Integer> ages, Set<String> addresses,
                        Map<String, String> friends);

}