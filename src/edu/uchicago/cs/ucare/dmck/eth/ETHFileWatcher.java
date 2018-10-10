package edu.uchicago.cs.ucare.dmck.eth;

import java.util.Properties;
import edu.uchicago.cs.ucare.dmck.event.Event;
import edu.uchicago.cs.ucare.dmck.server.FileWatcher;
import edu.uchicago.cs.ucare.dmck.server.ModelCheckingServerAbstract;

public class ETHFileWatcher extends FileWatcher {

    public ETHFileWatcher(String sPath, ModelCheckingServerAbstract dmck) {
        super(sPath, dmck);
    }

    @Override
    public synchronized void proceedEachFile(String filename, Properties ev) {
        if (filename.startsWith("eth-")) {
            int sendNode = Integer.parseInt(ev.getProperty("sendNode"));
            int recvNode = Integer.parseInt(ev.getProperty("recvNode"));
            String messageType = ev.getProperty("messageType");
            int pendingNonce = Integer.parseInt(ev.getProperty("pendingNonce"));
            int currentNonce = Integer.parseInt(ev.getProperty("currentNonce"));
            int eventId = Integer.parseInt(ev.getProperty("eventId"));

            LOG.info("Receive msg " + filename + " sendNode=" + sendNode
                    + " msgType=" + messageType
                    + " pendingNonce=" + pendingNonce + " currentNonce="+ currentNonce
                    + " eventId=" + eventId);

            appendReceivedUpdates("New Event: filename=" + filename + " sendNode=" + sendNode
                    +  " msgType=" + messageType
                    + " pendingNonce=" + pendingNonce + " currentNonce="+ currentNonce
                    + " eventId=" + eventId);

            long hashId = commonHashId(eventId);
            Event event = new Event(hashId);
            event.addKeyValue(Event.FROM_ID, sendNode);
            event.addKeyValue(Event.TO_ID, recvNode);
            event.addKeyValue(Event.FILENAME, filename);
            event.addKeyValue("messageType", messageType);
            event.addKeyValue("pendingNonce", pendingNonce);
            event.addKeyValue("currentNonce", currentNonce);
            event.setVectorClock(dmck.getVectorClock(sendNode, recvNode));

            dmck.localStates[sendNode].setKeyValue("pendingNonce", pendingNonce);
            dmck.localStates[sendNode].setKeyValue("currentNonce", currentNonce);

            //because it's a local event
            dmck.offerLocalEvent(event);

            // ignore to block
//            ignoreEvent(filename);

            //if it's a message, use offerPacket instead
        }

        removeProceedFile(filename);
    }

    @Override
    protected void sequencerEnablingSignal(Event packet) {
//        commonEnablingSignal(packet);
    }

}
