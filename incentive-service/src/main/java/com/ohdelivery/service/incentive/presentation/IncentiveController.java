package com.ohdelivery.service.incentive.presentation;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ohdelivery.common.passport.RoleCheck;
import com.ohdelivery.common.passport.RoleType;
import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.incentive.application.request.IncentiveRequest;
import com.ohdelivery.service.incentive.application.response.IncentiveResponse;
import com.ohdelivery.service.incentive.application.service.IncentiveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incentives")
public class IncentiveController {

	private final IncentiveService incentiveService;

	@RoleCheck({RoleType.MASTER, RoleType.RIDER})
	@GetMapping("/{incentiveId}")
	public ResponseEntity<ApiResponse<IncentiveResponse.GetIncentiveResponse>> getIncentive(
		@PathVariable UUID incentiveId
	) {
		IncentiveResponse.GetIncentiveResponse response =  incentiveService.getIncentive(incentiveId);
		return ResponseEntity.ok(ApiResponse.success(SuccessCode.INCENTIVE_GET_SUCCESS.getCode().toString(),SuccessCode.INCENTIVE_GET_SUCCESS.getMessage(),response));
	}

	@RoleCheck(RoleType.MASTER)
	@PutMapping("/{incentiveId}")
	public ResponseEntity<ApiResponse<String>> updateIncentive(
		@PathVariable UUID incentiveId,
		@RequestBody IncentiveRequest.updateIncentiveRequest request
	) {
		incentiveService.updateIncentive(incentiveId, request);
		return ResponseEntity.ok(ApiResponse.success(SuccessCode.INCENTIVE_UPDATED_SUCCESS.getCode().toString(),SuccessCode.INCENTIVE_UPDATED_SUCCESS.getMessage()));
	}

	@RoleCheck(RoleType.MASTER)
	@DeleteMapping("/{incentiveId}")
	public ResponseEntity<ApiResponse<IncentiveResponse.DeleteIncentiveResponse>> deleteIncentive(
		@PathVariable UUID incentiveId
	) {

		return ResponseEntity.ok(ApiResponse.success(SuccessCode.INCENTIVE_DELETED_SUCCESS.getCode().toString(),SuccessCode.INCENTIVE_DELETED_SUCCESS.getMessage(), incentiveService.deleteIncentive(incentiveId)));
	}


}
