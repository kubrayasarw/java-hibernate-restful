package com.example.resources.ihale;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Kubra on 7.03.2017.
 */

@Path("ihaleKatilim")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IhaleKatilimResource {

    @Inject
    IhaleKatilimDao ihaleKatilimDao;


    @Path("/ihaleKatilimGetir/{ihaleOid}")
    @GET
    @UnitOfWork(readOnly = true)
    public List<IhaleKatilim> IhaleKatilimGetir(@Auth Credentials credentials, @PathParam(value = "ihaleOid") String ihaleOid) {

        List<IhaleKatilim> ihaleList = ihaleKatilimDao.ihaleyeGoreKatilimGetir(ihaleOid);
        return ihaleList;
    }

    @Path("/ihaleKatilimSil/{ihaleOid}")
    @DELETE
    @UnitOfWork(readOnly = true)
    public List<IhaleKatilim> IhaleKatilimSil(@Auth Credentials credentials, @PathParam(value = "ihaleOid") String ihaleOid) {

        List<IhaleKatilim> ihaleList = ihaleKatilimDao.ihaleyeGoreKatilimGetir(ihaleOid);
        for (IhaleKatilim ik : ihaleList) {
            ihaleKatilimDao.delete(ik);
        }
        return ihaleList;
    }

    @PUT
    @UnitOfWork
    public IhaleKatilim create(@Valid IhaleKatilim ihaleKatilim, @Auth Credentials credentials) {
        ihaleKatilimDao.create(ihaleKatilim);
        return ihaleKatilim;
    }

    @POST
    @UnitOfWork
    public IhaleKatilim update(@Valid IhaleKatilim ihaleKatilim, @Auth Credentials credentials) {
        ihaleKatilimDao.update(ihaleKatilim);

        return ihaleKatilim;
    }

}
