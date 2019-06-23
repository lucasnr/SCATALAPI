package br.edu.ifrn.scatalapi.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class TutoriaUpdateDTO {

	private List<MatriculaDTO> tutores;
}
