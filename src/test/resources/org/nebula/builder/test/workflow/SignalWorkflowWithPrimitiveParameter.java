package org.nebula.builder.test.workflow;

import org.nebula.framework.annotation.Signal;
import org.nebula.framework.annotation.Start;
import org.nebula.framework.annotation.Workflow;

@Workflow
public interface SignalWorkflowWithPrimitiveParameter {

  @Start
  public void testBasic(String name);


  @Signal
  public void testSignal(int signal);

  @Signal
  public void testSignal(long signal);

  @Signal
  public void testSignal(float signal);

  @Signal
  public void testSignal(double signal);

  @Signal
  public void testSignal(byte signal);

  @Signal
  public void testSignal(char signal);

  @Signal
  public void testSignal(short signal);

  @Signal
  public void testSignal(boolean signal);

}