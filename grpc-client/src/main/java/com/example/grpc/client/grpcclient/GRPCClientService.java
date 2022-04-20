package com.example.grpc.client.grpcclient;

import java.io.*;

import com.example.grpc.server.grpcserver.PingRequest;
import com.example.grpc.server.grpcserver.PongResponse;
import com.example.grpc.server.grpcserver.PingPongServiceGrpc;
import com.example.grpc.server.grpcserver.MatrixRequest;
import com.example.grpc.server.grpcserver.MatrixReply;
import com.example.grpc.server.grpcserver.MatrixServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@Service
public class GRPCClientService {
	
	
    public String ping() {
        	ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();        
		PingPongServiceGrpc.PingPongServiceBlockingStub stub
                = PingPongServiceGrpc.newBlockingStub(channel);        
		PongResponse helloResponse = stub.ping(PingRequest.newBuilder()
                .setPing("")
                .build());        
		channel.shutdown();        
		return helloResponse.getPong();
    }
	// in the end points i provide the two matrices to the add and mult functions as arguments and then I set the entries of the matrix request 
	// to be the entries of the matrices provided
	// the two matrices are then either added or multiplied based on the function 
	// using the function provided in the server

    public String add(int[][]matrix1, int[][]matrix2){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
		.usePlaintext()
		.build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub
		 = MatrixServiceGrpc.newBlockingStub(channel);
		MatrixReply A=stub.addBlock(MatrixRequest.newBuilder()
			.setA00(matrix1[0][0])
			.setA01(matrix1[0][1])
			.setA10(matrix1[1][0])
			.setA11(matrix1[1][1])
			.setB00(matrix2[0][0])
			.setB01(matrix2[0][1])
			.setB10(matrix2[1][0])
			.setB11(matrix2[1][1])
			.build());
		String resp= A.getC00()+" "+A.getC01()+"<br>"+A.getC10()+" "+A.getC11()+"\n";
		return resp;
    }

	public String multiply(int[][]matrix1, int[][]matrix2){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
		.usePlaintext()
		.build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub
		 = MatrixServiceGrpc.newBlockingStub(channel);
		MatrixReply A=stub.multiplyBlock(MatrixRequest.newBuilder()
			.setA00(matrix1[0][0])
			.setA01(matrix1[0][1])
			.setA10(matrix1[1][0])
			.setA11(matrix1[1][1])
			.setB00(matrix2[0][0])
			.setB01(matrix2[0][1])
			.setB10(matrix2[1][0])
			.setB11(matrix2[1][1])
			.build());
		String resp= A.getC00()+" "+A.getC01()+"<br>"+A.getC10()+" "+A.getC11()+"\n";
		return resp;
    }

	
	
}
