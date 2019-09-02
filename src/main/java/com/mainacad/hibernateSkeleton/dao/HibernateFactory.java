package com.mainacad.hibernateSkeleton.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateFactory {
  private static SessionFactory sessionFactory;

  private HibernateFactory() {
  }

  public static SessionFactory getSessionFactory(){
    if (sessionFactory == null){
      Configuration configuration = new Configuration().configure();

      sessionFactory = configuration.buildSessionFactory();
    }

    return sessionFactory;
  }
}
