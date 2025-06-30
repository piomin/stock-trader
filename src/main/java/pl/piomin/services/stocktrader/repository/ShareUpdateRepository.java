package pl.piomin.services.stocktrader.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.services.stocktrader.model.entity.ShareUpdate;

public interface ShareUpdateRepository extends CrudRepository<ShareUpdate, Long> {

}
