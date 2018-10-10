package edu.uchicago.cs.ucare.dmck.eth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.uchicago.cs.ucare.dmck.server.ModelCheckingServerAbstract;
import edu.uchicago.cs.ucare.dmck.server.SpecVerifier;
import edu.uchicago.cs.ucare.dmck.util.LocalState;

public class ETHVerifier extends SpecVerifier {

    protected static final Logger LOG = LoggerFactory.getLogger(ETHVerifier.class);

    boolean error;

    public ETHVerifier() {
        error = false;
    }

    public ETHVerifier(ModelCheckingServerAbstract modelCheckingServer) {
        this.modelCheckingServer = modelCheckingServer;
    }

    @Override
    public boolean verify() {
//      Verify for eth-3412
        boolean result = false;
        for (LocalState state : modelCheckingServer.localStates) {
            if (state.getValue("pendingNonce") != null
                    && state.getValue("currentNonce") != null) {
                int pendingNonce = (int) state.getValue("pendingNonce");
                int currentNonce = (int) state.getValue("currentNonce");
                if (pendingNonce < currentNonce) {
                    LOG.info("@DMCK EthVerifier hit he bug",
                            pendingNonce, currentNonce);
                    result = true;
                }
            }
        }


        return result;
    }
}
            //Verify for eth-2793
//            if (state.getValue("state")!=null && state.getValue("info")!=null) {
//                int stateValue = (int) state.getValue("state");
//                if (stateValue==100){
//                    LOG.info("@huanke EthVerifier hit he bug");
//                    result = true;
//                    return result;
//                } else {
//                    LOG.info("@huanke verifyFalse");
//                    result = false;
//                    return result;
//                }
//            }
            //Verify for eth-15138
//             if (state.getValue("state")!=null && state.getValue("info")!=null) {
//                 int stateValue = (int) state.getValue("state");
//                 if (stateValue==100){
//                     LOG.info("@huanke EthVerifier hit he bug");
//                     result = true;
//                     return result;
//                 } else {
//                     LOG.info("@huanke verifyFalse");
//                     result = false;
//                     return result;
//                 }
//             }
// }
