package br.edu.ifrn.scatalapi.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class TutoriaUpdateDTO {

	private List<TutorUpdateDTO> tutores;
}
