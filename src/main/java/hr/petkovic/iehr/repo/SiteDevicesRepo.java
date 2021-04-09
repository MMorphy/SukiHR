package hr.petkovic.iehr.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.Device;
import hr.petkovic.iehr.entity.Site;
import hr.petkovic.iehr.entity.SiteDevices;

public interface SiteDevicesRepo extends JpaRepository<SiteDevices, Long>{

	public SiteDevices findBySiteAndDevice (Site s, Device d);
}
