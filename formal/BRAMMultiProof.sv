module BRAMMultiProof(
		      input [1:0]  io_rp_0_idx,
		      output [3:0] io_rp_0_data,
		      input [1:0]  io_rp_1_idx,
		      output [3:0] io_rp_1_data,
		      input [1:0]  io_wp_0_idx,
		      input 	   io_wp_0_en,
		      input [3:0]  io_wp_0_data,
		      input [1:0]  io_wp_1_idx,
		      input 	   io_wp_1_en,
		      input [3:0]  io_wp_1_data,
		      input 	   reset,
		      input 	   clk
		      );
    wire [3:0] 			   reg_1_io_rp_0_data;
    wire [3:0] 			   reg_1_io_rp_1_data;
    wire [3:0] 			   bram_io_rp_0_data;
    wire [3:0] 			   bram_io_rp_1_data;
    wire 			   init;

   Regfile reg_1 (
		  .io_rp_0_idx     (io_rp_0_idx[1:0]         ), //i
		  .io_rp_0_data    (reg_1_io_rp_0_data[3:0]  ), //o
		  .io_rp_1_idx     (io_rp_1_idx[1:0]         ), //i
		  .io_rp_1_data    (reg_1_io_rp_1_data[3:0]  ), //o
		  .io_wp_0_idx     (io_wp_0_idx[1:0]         ), //i
		  .io_wp_0_en      (io_wp_0_en               ), //i
		  .io_wp_0_data    (io_wp_0_data[3:0]        ), //i
		  .io_wp_1_idx     (io_wp_1_idx[1:0]         ), //i
		  .io_wp_1_en      (io_wp_1_en               ), //i
		  .io_wp_1_data    (io_wp_1_data[3:0]        ), //i
		  .clk             (clk                      ), //i
		  .reset           (reset                    )  //i
		  );
   BRAMMultiRegfile bram (
			  .io_rp_0_idx     (io_rp_0_idx[1:0]        ), //i
			  .io_rp_0_data    (bram_io_rp_0_data[3:0]  ), //o
			  .io_rp_1_idx     (io_rp_1_idx[1:0]        ), //i
			  .io_rp_1_data    (bram_io_rp_1_data[3:0]  ), //o
			  .io_wp_0_idx     (io_wp_0_idx[1:0]        ), //i
			  .io_wp_0_en      (io_wp_0_en              ), //i
			  .io_wp_0_data    (io_wp_0_data[3:0]       ), //i
			  .io_wp_1_idx     (io_wp_1_idx[1:0]        ), //i
			  .io_wp_1_en      (io_wp_1_en              ), //i
			  .io_wp_1_data    (io_wp_1_data[3:0]       ), //i
			  .clk             (clk                     ), //i
			  .reset           (reset                   )  //i
			  );
   assign init = $initstate();
   assign io_rp_0_data = bram_io_rp_0_data;
   assign io_rp_1_data = bram_io_rp_1_data;

   always @(posedge clk) begin
      if(init) begin
	 assume(reset);
	 assume(!io_wp_0_en);
	 assume(!io_wp_1_en);
      end else begin
	 assume(!reset);
	 assert(bram_io_rp_0_data == reg_1_io_rp_0_data);
	 assert(bram_io_rp_1_data == reg_1_io_rp_1_data);
      end
   end
endmodule

