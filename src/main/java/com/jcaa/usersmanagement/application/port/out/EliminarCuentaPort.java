package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.valueobject.UserId;

public interface EliminarCuentaPort {
  void delete(UserId userId);
}
