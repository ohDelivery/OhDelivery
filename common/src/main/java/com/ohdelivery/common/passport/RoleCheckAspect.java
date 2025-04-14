package com.ohdelivery.common.passport;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckAspect {

	private final HttpServletRequest request;
	private final ObjectMapper objectMapper;

	@Around("@annotation(roleCheck)")
	public Object checkRole(ProceedingJoinPoint joinPoint, RoleCheck roleCheck) throws Throwable {
		String passportHeader = request.getHeader(PassportConstant.PASSPORT_HEADER);
		Passport passport = objectMapper.readValue(passportHeader,Passport.class);

		List<String> roleNameList = Arrays.stream(roleCheck.value()).map(RoleType::getAuthority).toList();
		// 역할이 존재하지 않는 경우에는
		if(roleNameList.isEmpty()) {
			return joinPoint.proceed();
		}

		// 접근권한이 존재하지 않는경우
		if (!roleNameList.contains(passport.getRoleType().getAuthority())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
		}

		return joinPoint.proceed();
	}
}
