package com.example.resources.kurum;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Kubra on 2.03.2017.
 */

@Path("kurum")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KurumResource {
    @Inject
KurumDao kurumDao;

    @Path("/kurumGetir")
    @GET
    @UnitOfWork(readOnly = true)
    public List<Kurum> kurumGetir(@Auth Credentials credentials) {


        List<Kurum> kurumList = kurumDao.findAll(Kurum.class);
        return kurumList;
    }


    @PUT
    @UnitOfWork
    public Kurum create(@Valid Kurum kurum, @Auth Credentials credentials){



        kurumDao.create(kurum);

        return kurum;
    }

    @POST
    @UnitOfWork
    public Kurum update(@Valid Kurum kurum , @Auth Credentials credentials){



        kurumDao.update(kurum);

        return kurum;
    }
}
