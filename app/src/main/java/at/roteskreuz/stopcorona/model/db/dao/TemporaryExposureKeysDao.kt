package at.roteskreuz.stopcorona.model.db.dao

import androidx.room.*
import at.roteskreuz.stopcorona.model.entities.exposure.DbSentTemporaryExposureKeys
import at.roteskreuz.stopcorona.model.repositories.TemporaryExposureKeysWrapper
import io.reactivex.Flowable

/**
 * DAO to manage [DbSentTemporaryExposureKeys].
 */
@Dao
abstract class TemporaryExposureKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOrUpdateTemporaryExposureKey(record: DbSentTemporaryExposureKeys)

    @Query("SELECT * FROM sent_temporary_exposure_keys")
    abstract fun observeSentTemporaryExposureKeys(): Flowable<List<DbSentTemporaryExposureKeys>>

    @Transaction
    open suspend fun insertSentTemporaryExposureKeys(
        exposureKeys: List<TemporaryExposureKeysWrapper>
    ) {
        exposureKeys.forEach { exposureKeyWrapper ->
            insertOrUpdateTemporaryExposureKey(
                DbSentTemporaryExposureKeys(
                    exposureKeyWrapper.key.rollingPeriod,
                    exposureKeyWrapper.password
                )
            )
        }
    }

    @Query("DELETE FROM sent_temporary_exposure_keys")
    abstract suspend fun removeSentTemporaryExposureKeys()
}