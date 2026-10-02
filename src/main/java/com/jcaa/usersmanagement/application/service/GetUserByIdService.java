package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.ObtenerCuentaPorIdUseCase;
import com.jcaa.usersmanagement.application.port.out.ObtenerCuentaPorIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GetUserByIdService implements ObtenerCuentaPorIdUseCase {

  private final ObtenerCuentaPorIdPort ObtenerCuentaPorIdPort;
  private final Validator validator;

  @Override
  public UserModel execute(final GetUserByIdQuery query) {
    validateQuery(query);

    final UserId userId = UserApplicationMapper.fromGetUserByIdQueryToUserId(query);
    return ObtenerCuentaPorIdPort
        .getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }

  private void validateQuery(final GetUserByIdQuery query) {
    final Set<ConstraintViolation<GetUserByIdQuery>> violations = validator.validate(query);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
