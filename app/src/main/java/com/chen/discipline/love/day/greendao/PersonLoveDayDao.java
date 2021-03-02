package com.chen.discipline.love.day.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PERSON_LOVE_DAY".
*/
public class PersonLoveDayDao extends AbstractDao<PersonLoveDay, Long> {

    public static final String TABLENAME = "PERSON_LOVE_DAY";

    /**
     * Properties of entity PersonLoveDay.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DayName = new Property(1, String.class, "dayName", false, "DAY_NAME");
        public final static Property DayType = new Property(2, String.class, "dayType", false, "DAY_TYPE");
        public final static Property Day = new Property(3, String.class, "day", false, "DAY");
        public final static Property PersonId = new Property(4, long.class, "personId", false, "PERSON_ID");
    }


    public PersonLoveDayDao(DaoConfig config) {
        super(config);
    }
    
    public PersonLoveDayDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PERSON_LOVE_DAY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"DAY_NAME\" TEXT," + // 1: dayName
                "\"DAY_TYPE\" TEXT," + // 2: dayType
                "\"DAY\" TEXT," + // 3: day
                "\"PERSON_ID\" INTEGER NOT NULL );"); // 4: personId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PERSON_LOVE_DAY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PersonLoveDay entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String dayName = entity.getDayName();
        if (dayName != null) {
            stmt.bindString(2, dayName);
        }
 
        String dayType = entity.getDayType();
        if (dayType != null) {
            stmt.bindString(3, dayType);
        }
 
        String day = entity.getDay();
        if (day != null) {
            stmt.bindString(4, day);
        }
        stmt.bindLong(5, entity.getPersonId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PersonLoveDay entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String dayName = entity.getDayName();
        if (dayName != null) {
            stmt.bindString(2, dayName);
        }
 
        String dayType = entity.getDayType();
        if (dayType != null) {
            stmt.bindString(3, dayType);
        }
 
        String day = entity.getDay();
        if (day != null) {
            stmt.bindString(4, day);
        }
        stmt.bindLong(5, entity.getPersonId());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PersonLoveDay readEntity(Cursor cursor, int offset) {
        PersonLoveDay entity = new PersonLoveDay( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // dayName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // dayType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // day
            cursor.getLong(offset + 4) // personId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PersonLoveDay entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDayName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDayType(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDay(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPersonId(cursor.getLong(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PersonLoveDay entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PersonLoveDay entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PersonLoveDay entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
