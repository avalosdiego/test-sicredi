package com.avalos.sicredi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avalos.sicredi.model.Pauta;
import com.avalos.sicredi.model.StatusPauta;

public interface PautaRepository extends JpaRepository<Pauta, Long> {

	List<Pauta> findByStatus(StatusPauta status);

}
