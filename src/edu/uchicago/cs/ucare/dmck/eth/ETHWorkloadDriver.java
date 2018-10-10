package edu.uchicago.cs.ucare.dmck.eth;

import java.io.*;

import edu.uchicago.cs.ucare.dmck.server.WorkloadDriver;

public class ETHWorkloadDriver extends WorkloadDriver {

  private Process[] node;
  private Thread consoleWriter;
  private FileOutputStream[] consoleLog;

  private String logDir;

  public ETHWorkloadDriver(int numNode, String workingDir, String ipcDir, String samcDir,
      String targetSysDir) {
    super(numNode, workingDir, ipcDir, samcDir, targetSysDir);



    node = new Process[numNode];
    consoleLog = new FileOutputStream[numNode];
    consoleWriter = new Thread(new LogWriter());
    consoleWriter.start();
  }

  @Override
  public void startNode(int id) {

    logDir = workingDir + "/console/" + this.testId + "/" + id;

    try {
        node[id] = Runtime.getRuntime().exec(workingDir + "/startNode.sh " + ipcDir + " "
            + logDir + " " + id + " " + testId);
        LOG.info("Start eth node-" + id + "exec: "+workingDir + "/startNode.sh " + ipcDir + " "
            + logDir + " " + id + " " + testId);
        Thread.sleep(50);
    } catch (Exception e) {
        e.printStackTrace();
    }
  }

  @Override
  public void stopNode(int id) {
    LOG.info("Kill node-" + id);
    try {
      node[id] = Runtime.getRuntime().exec(workingDir + "/killNode.sh " + id);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

//    @Override
//    public void startWorkload() {
//        //do nothing
//    }
  @Override
  public void startWorkload() {
    try {
      LOG.debug("Start ETH Workload");
      //only execute node1 sendtransaction
      Runtime.getRuntime().exec(workingDir + "/startWorkload-trans.sh " + testId
      +" 1 ");
    } catch (Exception e) {
      LOG.error("Error in executing some of the commands");
    }
  }

  @Override
  public void stopWorkload() {
    // nothing
  }

  @Override
  public void resetTest(int testId) {
    clearIPCDir();
    this.testId = testId;
    try {
      Runtime.getRuntime().exec(workingDir + "/resettest " + this.testId).waitFor();
    } catch (Exception e) {
      LOG.error("Error in cexecuting resettest script");
    }
    for (int i = 0; i < numNode; ++i) {
      if (consoleLog[i] != null) {
        try {
          consoleLog[i].close();
        } catch (IOException e) {
          LOG.error("", e);
        }
      }
      try {
        File directory = new File(workingDir + "/console/" + this.testId + "/" + i);
        if (! directory.exists()) {
          directory.mkdirs();
        }
        consoleLog[i] = new FileOutputStream(workingDir + "/console/" + this.testId + "/"
                + i + "/output.log");
      } catch (FileNotFoundException e) {
        LOG.error("", e);
      }
    }
  }

  class LogWriter implements Runnable {

    public void run() {
      byte[] buff = new byte[256];
      while (true) {
        for (int i = 0; i < numNode; ++i) {
          if (node[i] != null) {
            int r = 0;
            InputStream stdout = node[i].getInputStream();
            InputStream stderr = node[i].getErrorStream();
            try {
              while ((r = stdout.read(buff)) != -1) {
                consoleLog[i].write(buff, 0, r);
                consoleLog[i].flush();
              }
              while ((r = stderr.read(buff)) != -1) {
                consoleLog[i].write(buff, 0, r);
                consoleLog[i].flush();
              }
            } catch (IOException e) {
              // LOG.debug("", e);
            }
          }
        }
        try {
          Thread.sleep(300);
        } catch (InterruptedException e) {
          LOG.warn("", e);
        }
      }
    }

  }

}
