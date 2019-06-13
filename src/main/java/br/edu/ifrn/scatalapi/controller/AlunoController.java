/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.edu.ifrn.scatalapi.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dao.AlunoDAO;
import br.edu.ifrn.scatalapi.dao.DAOFactory;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Token;
import br.edu.ifrn.scatalapi.model.dto.AlunoDTO;

@RestController
public class AlunoController {

	@GetMapping(value = "/aluno/{matricula}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AlunoDTO get(@RequestHeader("token") String tokenConteudo, @PathVariable String matricula) {
		Token tokenObject = new Token(tokenConteudo);
		if (!tokenObject.isValido()) {
			return null;
		}

		AlunoDAO dao = DAOFactory.getAlunoDAO();
		Aluno aluno = dao.buscaPorMatricula(matricula);
		dao.close();
		return new AlunoDTO(aluno);
	}

}
