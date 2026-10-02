package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.application.port.in.CrearCuentaUseCase;
import com.jcaa.usersmanagement.application.port.in.EliminarCuentaUseCase;
import com.jcaa.usersmanagement.application.port.in.ObtenerTodasLasCuentasUseCase;
import com.jcaa.usersmanagement.application.port.in.ObtenerCuentaPorIdUseCase;
import com.jcaa.usersmanagement.application.port.in.ActualizarCuentaUseCase;
import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateUserRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateUserRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.UserRestResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper.UserRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController implements UserRestControllerDocs {

  private final CrearCuentaUseCase CrearCuentaUseCase;
  private final ActualizarCuentaUseCase ActualizarCuentaUseCase;
  private final EliminarCuentaUseCase EliminarCuentaUseCase;
  private final ObtenerCuentaPorIdUseCase ObtenerCuentaPorIdUseCase;
  private final ObtenerTodasLasCuentasUseCase ObtenerTodasLasCuentasUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserRestResponse create(@Valid @RequestBody final CreateUserRestRequest request) {
    final CreateUserCommand command = UserRestMapper.toCreateCommand(request);
    final UserModel user = CrearCuentaUseCase.execute(command);
    return UserRestMapper.toResponse(user);
  }

  @GetMapping
  public List<UserRestResponse> getAll() {
    return UserRestMapper.toResponseList(ObtenerTodasLasCuentasUseCase.execute());
  }

  @GetMapping("/{id}")
  public UserRestResponse getById(@PathVariable final String id) {
    final GetUserByIdQuery query = UserRestMapper.toGetByIdQuery(id);
    final UserModel user = ObtenerCuentaPorIdUseCase.execute(query);
    return UserRestMapper.toResponse(user);
  }

  @PutMapping("/{id}")
  public UserRestResponse update(
      @PathVariable final String id,
      @Valid @RequestBody final UpdateUserRestRequest request) {
    final UpdateUserCommand command = UserRestMapper.toUpdateCommand(id, request);
    final UserModel user = ActualizarCuentaUseCase.execute(command);
    return UserRestMapper.toResponse(user);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable final String id) {
    final DeleteUserCommand command = UserRestMapper.toDeleteCommand(id);
    EliminarCuentaUseCase.execute(command);
  }
}

