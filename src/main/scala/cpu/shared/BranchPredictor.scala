package cpu.shared

import cpu.interfaces.{BranchControl, BPFetchRequestInterface, BPFetchResponseInterface}

import util._
import spinal.core._
import spinal.lib._


class BranchPredictor extends Component {
  val io = new Bundle {
    val b_ctrl = slave(new BranchControl)

    val fetch_req_interface = new BPFetchRequestInterface()
    val fetch_resp_interface = new BPFetchResponseInterface()
  }

  io.fetch_req_interface.branch.valid := False
  io.fetch_req_interface.branch.payload := 0

  when(io.b_ctrl.branch_taken){
    io.fetch_req_interface.branch.payload := io.b_ctrl.target_addr
    io.fetch_req_interface.branch.valid := True
  }


}
