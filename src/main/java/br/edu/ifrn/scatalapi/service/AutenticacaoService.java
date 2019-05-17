package br.edu.ifrn.scatalapi.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.edu.ifrn.scatalapi.model.AlunoLoginDTO;
import br.edu.ifrn.scatalapi.model.AutenticacaoToken;

@Path("/login")
public class AutenticacaoService {

	@POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public AutenticacaoToken post(AlunoLoginDTO aluno) {
    	return new AutenticacaoToken(aluno);
    }
}
