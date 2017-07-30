package com.example.resources.ihale;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Kubra on 23.02.2017.
 */

@Path("ihaleDetay")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IhaleDetayResource {
    @Inject
    IhaleDetayDao ihaleDetayDao;




    @Path("/IhaleDetayGetir/{ihaleOid}")
    @GET
    @UnitOfWork(readOnly = true)
    public List<IhaleDetay> IhaleDetayGetir(@Auth Credentials credentials,@PathParam(value="ihaleOid") String ihaleOid) {

        List<IhaleDetay> ihaleList = ihaleDetayDao.ihaleyeGoreDetayGetir(ihaleOid);
        return ihaleList;
    }



    @PUT
    @UnitOfWork
    public IhaleDetay create(@Valid IhaleDetay ihaleDetay , @Auth Credentials credentials){



        ihaleDetayDao.create(ihaleDetay);

        return ihaleDetay;
    }

    @POST
    @UnitOfWork
    public IhaleDetay update(@Valid IhaleDetay ihaleDetay , @Auth Credentials credentials){



        ihaleDetayDao.update(ihaleDetay);

        return ihaleDetay;
    }
    @DELETE
    @UnitOfWork
    public IhaleDetay delete(@Valid IhaleDetay ihaleDetay, @Auth Credentials credentials) {


        ihaleDetayDao.delete(ihaleDetay);

        return ihaleDetay;
    }
}
