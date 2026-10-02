package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import com.jcaa.usersmanagement.application.port.in.CrearCuentaUseCase;
import com.jcaa.usersmanagement.application.port.in.EliminarCuentaUseCase;
import com.jcaa.usersmanagement.application.port.in.ObtenerTodasLasCuentasUseCase;
import com.jcaa.usersmanagement.application.port.in.ObtenerCuentaPorIdUseCase;
import com.jcaa.usersmanagement.application.port.in.LoginCuentaUseCase;
import com.jcaa.usersmanagement.application.port.in.ActualizarCuentaUseCase;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateUserRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.LoginRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateUserRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper.UserDesktopMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class UserController {

  private final CrearCuentaUseCase CrearCuentaUseCase;
  private final ActualizarCuentaUseCase ActualizarCuentaUseCase;
  private final EliminarCuentaUseCase EliminarCuentaUseCase;
  private final ObtenerCuentaPorIdUseCase ObtenerCuentaPorIdUseCase;
  private final ObtenerTodasLasCuentasUseCase ObtenerTodasLasCuentasUseCase;
  private final LoginCuentaUseCase LoginCuentaUseCase;

  public List<UserResponse> listAllUsers() {
    final var users = ObtenerTodasLasCuentasUseCase.execute();
    return UserDesktopMapper.toResponseList(users);
  }

  public UserResponse findUserById(final String id) {
    final var query = UserDesktopMapper.toGetByIdQuery(id);
    final var user = ObtenerCuentaPorIdUseCase.execute(query);
    return UserDesktopMapper.toResponse(user);
  }

  public UserResponse createUser(final CreateUserRequest request) {
    final var command = UserDesktopMapper.toCreateCommand(request);
    final var user = CrearCuentaUseCase.execute(command);
    return UserDesktopMapper.toResponse(user);
  }

  public UserResponse updateUser(final UpdateUserRequest request) {
    final var command = UserDesktopMapper.toUpdateCommand(request);
    final var user = ActualizarCuentaUseCase.execute(command);
    return UserDesktopMapper.toResponse(user);
  }

  public void deleteUser(final String id) {
    final var command = UserDesktopMapper.toDeleteCommand(id);
    EliminarCuentaUseCase.execute(command);
  }

  public UserResponse login(final LoginRequest request) {
    final var command = UserDesktopMapper.toLoginCommand(request);
    final var user = LoginCuentaUseCase.execute(command);
    return UserDesktopMapper.toResponse(user);
  }
}
