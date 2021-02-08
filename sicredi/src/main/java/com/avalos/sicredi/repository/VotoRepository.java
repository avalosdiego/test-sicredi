package com.avalos.sicredi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avalos.sicredi.model.Pauta;
import com.avalos.sicredi.model.TipoVoto;
import com.avalos.sicredi.model.Voto;

public interface VotoRepository extends JpaRepository<Voto, Long> {

	@Query("SELECT v FROM Voto v JOIN v.pauta p WHERE v.tipo = :tipo AND v.pauta = :pauta")
	List<Voto> findTipoVotoAndPautaId(@Param("tipo") TipoVoto tipo, @Param("pauta") Pauta pauta);

	@Query("SELECT v FROM Voto v JOIN v.pauta p WHERE v.usuarioId = :usuarioId AND v.pauta = :pauta")
	Voto findByPautaIdAndUsuarioId(@Param("pauta") Pauta pauta, @Param("usuarioId") String usuarioId);

}
