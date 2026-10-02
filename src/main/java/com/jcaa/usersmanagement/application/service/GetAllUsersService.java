package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.ObtenerTodasLasCuentasUseCase;
import com.jcaa.usersmanagement.application.port.out.ObtenerTodasLasCuentasPort;
import com.jcaa.usersmanagement.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllUsersService implements ObtenerTodasLasCuentasUseCase {

  private final ObtenerTodasLasCuentasPort ObtenerTodasLasCuentasPort;

  @Override
  public List<UserModel> execute() {
    return ObtenerTodasLasCuentasPort.getAll();
  }
}
