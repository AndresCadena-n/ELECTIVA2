package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.domain.model.UserModel;
import java.util.List;

public interface ObtenerTodasLasCuentasUseCase {
  List<UserModel> execute();
}
