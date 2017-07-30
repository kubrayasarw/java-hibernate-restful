package com.example.resources.ihale;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Kubra on 29.03.2017.
 */
@Path("IhaleYorum")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IhaleYorumResource {
    @Inject
    private IhaleYorumDao ihaleYorumDao;
@Inject
private PersonelDao personelDao;
    @Path("/ihaleyeGoreYorumGetir/{ihaleOid}")
    @GET
    @UnitOfWork
    public List<IhaleYorum> ihaleyeGoreYorumGetir(@Auth Credentials credentials, @PathParam(value = "ihaleOid") String ihaleOid) {
        List<IhaleYorum> ihaleYorumList = ihaleYorumDao.ihaleyeGoreYorumGetir(ihaleOid);

        return ihaleYorumList;

    }

    @PUT
    @UnitOfWork
    public IhaleYorum create(@Auth Credentials credentials, @Valid IhaleYorum ihaleYorum) {
         ihaleYorum.setPersonel(personelDao.personelGetirKullaniciyaGore(credentials.getUserId()));
        return ihaleYorumDao.create(ihaleYorum);
    }

    @POST
    @UnitOfWork
    public IhaleYorum update(@Auth Credentials credentials,@Valid IhaleYorum ihaleYorum){

        return ihaleYorumDao.update(ihaleYorum);

    }

}
