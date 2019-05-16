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

package br.edu.ifrn.scatalapi.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.edu.ifrn.scatalapi.model.Aluno;

@Path("/aluno")
public class AlunoService {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Aluno> get() {
		Aluno aluno1 = new Aluno("Lucas", "20161164010023");
		Aluno aluno2 = new Aluno("Maria", "20161164010015");
		Aluno aluno3 = new Aluno("Daniel", "20161164010033");
		
		List<Aluno> alunos = new ArrayList<>();
		alunos.add(aluno1);
		alunos.add(aluno2);
		alunos.add(aluno3);
		
		return alunos;
    }

    @POST
    @Path("/salvar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(Aluno aluno) {
    	System.out.println(aluno.getNome() + ": " + aluno.getMatricula());
    	return Response.ok().build();
    }

    @PUT
    @Path("/")
    public void put() {
        // TODO: Implementation for HTTP PUT request
        System.out.println("PUT invoked");
    }

    @DELETE
    @Path("/")
    public void delete() {
        // TODO: Implementation for HTTP DELETE request
        System.out.println("DELETE invoked");
    }
}
