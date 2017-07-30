package com.example.resources.ihale;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Kubra on 21.02.2017.
 */

@Path("ihale")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IhaleResource {
    @Inject
    IhaleDao ıhaleDao;

    @Path("/IhaleGetir")
    @GET
    @UnitOfWork(readOnly = true)
    public List<Ihale> IhaleGetir(@Auth Credentials credentials) {

        List<Ihale> ihaleList = ıhaleDao.findAll(Ihale.class);
        return ihaleList;
    }
    @Path("/IhaleGetirıIkn/{ikn}")
    @GET
    @UnitOfWork(readOnly = true)
    public List<Ihale> ikneGoreIhaleGetir(@Auth Credentials credentials, @PathParam(value = "ikn") String ikn) {

        List<Ihale> ihaleList = ıhaleDao.ikneGoreIhaleGetir(ikn);
        return ihaleList;
    }

    @Path("/IhaleGetir/{aktiflikDurum}")
    @GET
    @UnitOfWork(readOnly = true)
    public List<Ihale> aktiflikDurumaGoreIhaleGetir(@Auth Credentials credentials, @PathParam(value = "aktiflikDurum") Boolean aktiflikDurum) {

        List<Ihale> ihaleList = ıhaleDao.aktiflikDurumaGoreIhaleGetir(aktiflikDurum);
        return ihaleList;
    }


    @PUT
    @UnitOfWork
    public Ihale create(@Valid Ihale ihale , @Auth Credentials credentials){
        ıhaleDao.create(ihale);
        return ihale;
    }

    @POST
    @UnitOfWork
    public Ihale update(@Valid Ihale ihale , @Auth Credentials credentials){
         return ıhaleDao.update(ihale);
    }





}
