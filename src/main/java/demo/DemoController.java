package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping(path = "/")
public class DemoController {

	private DemoRepository demoRepository;

	@Autowired
	public DemoController(DemoRepository demoRepository) {
		this.demoRepository = requireNonNull(demoRepository);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Demo> listDemos() {
		return this.demoRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	public void createDemo() {
		Demo demo = new Demo(UUID.randomUUID().toString());
		this.demoRepository.save(demo);
	}

}
