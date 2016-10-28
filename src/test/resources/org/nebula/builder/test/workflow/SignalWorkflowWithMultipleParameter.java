package org.nebula.builder.test.workflow;

import org.nebula.framework.annotation.Signal;
import org.nebula.framework.annotation.Start;
import org.nebula.framework.annotation.Workflow;

@Workflow
public interface SignalWorkflowWithMultipleParameter {

  @Start
  public void testStart(String name);

  @Signal
  public void testSignal();

  @Signal
  public void testSignal(String signal);

  @Signal
  public void testSignal(String signal1, String signal2);

}