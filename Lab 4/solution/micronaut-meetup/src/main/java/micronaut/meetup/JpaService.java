package micronaut.meetup;

import micronaut.meetup.model.Model;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Singleton
public class JpaService {

    EntityManager entityManager;

    public JpaService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Model getModel(String id){
        return entityManager.find(Model.class, id);
    }

    @Transactional
    public void saveModel(String name){
        Model model = new Model();
        model.setId(name);
        entityManager.merge(model);
    }


}
