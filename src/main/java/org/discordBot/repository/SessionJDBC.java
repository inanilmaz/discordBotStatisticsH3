package org.discordBot.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.discordBot.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class SessionJDBC {
    private final Session session;
    private SessionFactory sessionFactory;
    private final Transaction tx ;
    public SessionJDBC() {
        sessionFactory = HibernateUtil.getSessionFactory();
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
    }

    public List<CastleTable> getAllCastles(){
        String sqlCastle = "FROM " + CastleTable.class.getSimpleName();
        return session.createQuery(sqlCastle).getResultList();
    }
    public List<HeroesTable> getAllHeroesInThisCastle(int id){
        String sqlHeroesInCastle = "FROM " + HeroesTable.class.getSimpleName()+
                " WHERE castleId = "+ id;
        return session.createQuery(sqlHeroesInCastle).getResultList();
    }
    public List<HeroesTable> getAllHeroes(){
        String sqlHeroes = "From " + HeroesTable.class.getSimpleName();
        return session.createQuery(sqlHeroes).getResultList();
    }
    public void addResultInGameTable(String[] addResultCommand){
        GameTable game = new GameTable();
        game.setResult(ResultGameEnum.WIN);
        List<UserTable> users = getAllUsers();
        for (UserTable user : users){
            if(user.getUserName().equalsIgnoreCase(addResultCommand[2])){
                game.setRival(user);
            }if(user.getUserName().equalsIgnoreCase(addResultCommand[0])){
                game.setUser(user);
            }
        }
        List<CastleTable> castles = getAllCastles();
        for(CastleTable ct :castles){
            if(ct.getName().equalsIgnoreCase(addResultCommand[1])){
                game.setCastle(ct);
            }
            if(ct.getName().equalsIgnoreCase(addResultCommand[3])){
                game.setRivalCastle(ct);
            }
        }
        session.save(game);
    }
    public boolean addResultWithOutOpp(String[] addResultCommand) {
        GameTable game = new GameTable();
        List<UserTable> users = getAllUsers();
        List<CastleTable> castles = getAllCastles();

        for (UserTable user : users) {
            if (user.getUserName().equalsIgnoreCase(addResultCommand[0])) {
                if (addResultCommand[3].equalsIgnoreCase("win")) {
                    game.setUser(user);
                } else if (addResultCommand[3].equalsIgnoreCase("lose")) {
                    game.setRival(user);
                }
            }
            if (user.getUserName().equalsIgnoreCase("N/A")) {
                if (addResultCommand[3].equalsIgnoreCase("win")) {
                    game.setRival(user);
                } else if (addResultCommand[3].equalsIgnoreCase("lose")) {
                    game.setUser(user);
                }
            }
        }

        for (CastleTable ct : castles) {
            if (ct.getName().equalsIgnoreCase(addResultCommand[1])) {
                if (addResultCommand[3].equalsIgnoreCase("win")) {
                    game.setCastle(ct);
                } else if (addResultCommand[3].equalsIgnoreCase("lose")) {
                    game.setRivalCastle(ct);
                }
            }
            if (ct.getName().equalsIgnoreCase(addResultCommand[2])) {
                if (addResultCommand[3].equalsIgnoreCase("win")) {
                    game.setRivalCastle(ct);
                } else if (addResultCommand[3].equalsIgnoreCase("lose")) {
                    game.setCastle(ct);
                }
            }
        }

        if (addResultCommand[3].equalsIgnoreCase("win")) {
            game.setResult(ResultGameEnum.WIN);
        } else if (addResultCommand[3].equalsIgnoreCase("lose")) {
            game.setResult(ResultGameEnum.LOSE);
        } else {
            return false;
        }

        session.save(game);
        return true;
    }

    public List<UserTable> getAllUsers(){
        String sqlUsers ="From " + UserTable.class.getSimpleName();
        return session.createQuery(sqlUsers).getResultList();
    }
    public boolean registrationUser(String userName, String urlAvatar){
        UserTable user = new UserTable();
        user.setUserName(userName.toLowerCase());
        user.setAvatarUrl(urlAvatar);
        user.setRegistrationTime(LocalDateTime.now());
        if(isRegistration(userName)){
            return true;
        }else {
            session.save(user);
            return false;
        }
    }
    public UserTable getIdByName(String name){
        String hqlUser = "From " + UserTable.class.getSimpleName()
                + " WHERE userName = '" + name + "'";
        List<UserTable> user = session.createQuery(hqlUser).getResultList();
        return user.get(0);
    }
    public List<GameTable> getGameByUserId(int userId){
        String hqlGame = "From " + GameTable.class.getSimpleName()
                + " WHERE user = " + userId;
        return session.createQuery(hqlGame).getResultList();
    }
    public List<GameTable> getGameByRivalId(int userId){
        String hqlGame = "From " + GameTable.class.getSimpleName()
                + " WHERE rival = " +userId;
        return session.createQuery(hqlGame).getResultList();
    }
    public boolean isRegistration(String userName){
        String hqlAllUsers = "From " + UserTable.class.getSimpleName();
        List<UserTable> users = session.createQuery(hqlAllUsers).getResultList();
        for(int i =0 ; i<users.size() ; i++){
            if(users.get(i).getUserName().equalsIgnoreCase(userName)){
                return true;
            }
        }
        return false;
    }


    public Long getCountOfGamesByUserIdAndCastleId(int userId, int castleId) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<GameTable> root = query.from(GameTable.class);
        query.select(builder.count(root));
        query.where(
                builder.and(
                        builder.equal(root.get("user").get("id"), userId),
                        builder.equal(root.get("castle").get("id"), castleId)
                )
        );

        return session.createQuery(query).uniqueResult();
    }
    public Long getCountOfGamesByRivalIdAndRivalCastleId(int rivalId, int rivalCastleId) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<GameTable> root = query.from(GameTable.class);
        query.select(builder.count(root));
        query.where(
                builder.and(
                        builder.equal(root.get("rival").get("id"), rivalId),
                        builder.equal(root.get("rivalCastle").get("id"), rivalCastleId)
                )
        );

        return session.createQuery(query).uniqueResult();
    }
    public List<GameTable> getGameTableByUserIdAndRivalId(int userId,int rivalId){
        String hqlQuery = "SELECT g FROM GameTable g "+
                "WHERE g.user.id = :userId " +
                "AND g.rival.id = :rivalId";
        Query query = session.createQuery(hqlQuery);
        query.setParameter("userId",userId);
        query.setParameter("rivalId",rivalId);
        return query.getResultList();
    }

    public List<GameTable> getAllCastleInGameByUserId(int userId) {
        String hqlQuery = "SELECT g FROM GameTable g " +
                "WHERE g.user.id = :userId ";
        Query query = session.createQuery(hqlQuery);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    public List<GameTable> getAllRivalCastleGameByUserId(int userId){
        String hqlQuery = "SELECT g FROM GameTable g " +
                "WHERE g.rival.id = :rivalId ";
        Query query = session.createQuery(hqlQuery);
        query.setParameter("rivalId",userId);
        return query.getResultList();
    }
    public CastleTable getCastleByName(String castleName){
        String hqlCastle = "SELECT c FROM CastleTable c " +
                "WHERE c.name = :castleName";
        Query query = session.createQuery(hqlCastle);
        query.setParameter("castleName",castleName);
        return (CastleTable) query.getSingleResult();
    }
    public void closeSessionAndTransaction(){
        session.flush();
        tx.commit();
        session.close();
        sessionFactory.close();
    }

}
