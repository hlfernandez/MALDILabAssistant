package es.uvigo.ei.sing.mla.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.uvigo.ei.sing.mla.daos.SampleDAO;
import es.uvigo.ei.sing.mla.model.entities.Sample;

@Service("sampleService")
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SampleServiceImpl implements SampleService {
	@Autowired
	SampleDAO dao;

	@Override
	public Sample add(Sample sample) {
		return dao.add(sample);
	}

	@Override
	public Sample get(Integer id) {
		return dao.get(id);
	}

	@Override
	public Sample update(Sample sample) {
		return dao.update(sample);
	}

	@Override
	public void delete(Sample sample) {
		dao.delete(sample);
	}
}
