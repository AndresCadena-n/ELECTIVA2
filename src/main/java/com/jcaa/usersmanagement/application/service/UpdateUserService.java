package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.ActualizarCuentaUseCase;
import com.jcaa.usersmanagement.application.port.out.ObtenerCuentaPorEmailPort;
import com.jcaa.usersmanagement.application.port.out.ObtenerCuentaPorIdPort;
import com.jcaa.usersmanagement.application.port.out.ActualizarCuentaPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UpdateUserService implements ActualizarCuentaUseCase {

  private final ActualizarCuentaPort ActualizarCuentaPort;
  private final ObtenerCuentaPorIdPort ObtenerCuentaPorIdPort;
  private final ObtenerCuentaPorEmailPort ObtenerCuentaPorEmailPort;
  private final EmailNotificationService emailNotificationService;
  private final Validator validator;

  @Override
  public UserModel execute(final UpdateUserCommand command) {
    validateCommand(command);

    final UserId userId = new UserId(command.id());
    final UserModel current = findExistingUserOrFail(userId);
    final UserEmail newEmail = new UserEmail(command.email());

    ensureEmailIsNotTakenByAnotherUser(newEmail, userId);

    final UserModel userToUpdate =
        UserApplicationMapper.fromUpdateCommandToModel(command, current.getPassword());
    final UserModel updatedUser = ActualizarCuentaPort.update(userToUpdate);

    emailNotificationService.notifyUserUpdated(updatedUser);

    return updatedUser;
  }

  private void validateCommand(final UpdateUserCommand command) {
    final Set<ConstraintViolation<UpdateUserCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private UserModel findExistingUserOrFail(final UserId userId) {
    return ObtenerCuentaPorIdPort
        .getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }

  private void ensureEmailIsNotTakenByAnotherUser(final UserEmail newEmail, final UserId ownerId) {
    ObtenerCuentaPorEmailPort
        .getByEmail(newEmail)
        .ifPresent(
            found -> {
              if (!found.getId().equals(ownerId)) {
                throw UserAlreadyExistsException.becauseEmailAlreadyExists(newEmail.value());
              }
            });
  }
}
