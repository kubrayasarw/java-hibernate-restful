package com.example.resources.ihale;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Kubra on 14.03.2017.
 */
@Path("IhaleSonuc")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IhaleSonucResource {
    @Inject
    IhaleSonucDao ihaleSonucDao;

    @Path("/ihaleyeGoreSonucGetir/{ihaleOid}")
    @GET
    @UnitOfWork
    public List<IhaleSonuc> ihaleyeGoreSonucGetir(@Auth Credentials credentials, @PathParam(value = "ihaleOid") String ihaleOid) {
        List<IhaleSonuc> ihaleSonucList = ihaleSonucDao.ihaleyeGoreSonucGetir(ihaleOid);

        return ihaleSonucList;

    }

    @PUT
    @UnitOfWork
    public IhaleSonuc create(@Valid IhaleSonuc ihaleSonuc, @Auth Credentials credentials) {


        ihaleSonucDao.create(ihaleSonuc);

        return ihaleSonuc;
    }

    @POST
    @UnitOfWork
    public IhaleSonuc update(@Valid IhaleSonuc ihaleSonuc, @Auth Credentials credentials) {


        ihaleSonucDao.update(ihaleSonuc);

        return ihaleSonuc;
    }

    @DELETE
    @UnitOfWork
    public IhaleSonuc delete(@Valid IhaleSonuc ihaleSonuc, @Auth Credentials credentials) {


        ihaleSonucDao.delete(ihaleSonuc);

        return ihaleSonuc;
    }

}
