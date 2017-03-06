package demo.support

import org.jooq.*
import org.jooq.impl.DSL
import java.sql.SQLFeatureNotSupportedException
import java.sql.Types
import java.util.*

// Binding for T : Any(JSONB) and String : Jackson's object
class PostgresJacksonBinding : Binding<Any, String> {
    /**
     * Generate SQL code for the bind variable.
     *
     *
     * Implementations should generate SQL code onto
     * [BindingSQLContext.render], given the context's bind variable
     * located at [BindingSQLContext.value]. Examples of such SQL code
     * are:
     *
     *  * `"?"`: Default implementations can simply generate a
     * question mark.<br></br>
     * <br></br>
     *
     *  * `"123"`: Implementations may choose to inline bind
     * variables to influence execution plan generation.<br></br>
     * <br></br>
     * [RenderContext.paramType] contains information whether inlined
     * bind variables are expected in the current context.<br></br>
     * <br></br>
     *
     *  * `"CAST(? AS DATE)"`: Cast a database to a more specific
     * type. This can be useful in databases like Oracle, which map both
     * `DATE` and `TIMESTAMP` SQL types to
     * [Timestamp].<br></br>
     * <br></br>
     * [RenderContext.castMode] may contain some hints about whether
     * casting is suggested in the current context.<br></br>
     * <br></br>
     *
     *  * `"?::json"`: Vendor-specific bind variables can be
     * supported, e.g. [SQLDialect.POSTGRES]'s JSON data type.
     *
     *
     *
     * Implementations must provide consistent behaviour between
     * [.sql] and
     * [.set], i.e. when bind variables are
     * inlined, then they must not be bound to the [PreparedStatement] in
     * [.set]

     * @param ctx The context object containing all argument objects.
     * *
     * @throws SQLException Implementations are allowed to pass on all
     * *             [SQLException]s to the caller to be wrapped in
     * *             [DataAccessException]s.
     */
    override fun sql(ctx: BindingSQLContext<String>) {
        ctx.render().visit(DSL.`val`(ctx.convert(converter()).value())).sql("::json")
    }

    /**
     * A converter that can convert between the database type and the custom
     * type.
     */
    override fun converter() = object : Converter<Any, String> {
        /**
         * The user type
         */
        override fun toType(): Class<String> = String::class.java

        /**
         * Convert a user object to a database object

         * @param userObject The user object
         * *
         * @return The database object
         */
        override fun to(userObject: String?): Any? = userObject

        /**
         * Convert a database object to a user object

         * @param databaseObject The database object
         * *
         * @return The user object
         */
        override fun from(databaseObject: Any?): String? = if (databaseObject == null) null else "" + databaseObject

        /**
         * The database type
         */
        override fun fromType(): Class<Any> = Any::class.java

    }

    /**
     * Get a [SQLInput]'s `OUT` value.
     *
     *
     * Implementations are expected to produce a value by calling
     * [BindingGetSQLInputContext.value], passing the resulting
     * value to the method.

     * @param ctx The context object containing all argument objects.
     * *
     * @throws SQLException Implementations are allowed to pass on all
     * *             [SQLException]s to the caller to be wrapped in
     * *             [DataAccessException]s.
     */
    override fun get(ctx: BindingGetSQLInputContext<String>?) = throw SQLFeatureNotSupportedException()

    /**
     * Get a [ResultSet]'s `OUT` value.
     *
     *
     * Implementations are expected to produce a value by calling
     * [BindingGetResultSetContext.value], passing the resulting
     * value to the method.

     * @param ctx The context object containing all argument objects.
     * *
     * @throws SQLException Implementations are allowed to pass on all
     * *             [SQLException]s to the caller to be wrapped in
     * *             [DataAccessException]s.
     */
    override fun get(ctx: BindingGetResultSetContext<String>) {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()))
    }

    /**
     * Get a [CallableStatement]'s `OUT` value.
     *
     *
     * Implementations are expected to produce a value by calling
     * [BindingGetStatementContext.value], passing the resulting
     * value to the method.

     * @param ctx The context object containing all argument objects.
     * *
     * @throws SQLException Implementations are allowed to pass on all
     * *             [SQLException]s to the caller to be wrapped in
     * *             [DataAccessException]s.
     */
    override fun get(ctx: BindingGetStatementContext<String>) {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()))
    }

    /**
     * Set a [SQLOutput]'s `IN` parameter.

     * @param ctx The context object containing all argument objects.
     * *
     * @throws SQLException Implementations are allowed to pass on all
     * *             [SQLException]s to the caller to be wrapped in
     * *             [DataAccessException]s.
     */
    override fun set(ctx: BindingSetSQLOutputContext<String>) = throw SQLFeatureNotSupportedException()

    /**
     * Set a [PreparedStatement]'s `IN` parameter.

     * @param ctx The context object containing all argument objects.
     * *
     * @throws SQLException Implementations are allowed to pass on all
     * *             [SQLException]s to the caller to be wrapped in
     * *             [DataAccessException]s.
     */
    override fun set(ctx: BindingSetStatementContext<String>) {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null))
    }

    /**
     * Register a [CallableStatement]'s `OUT` parameter.

     * @param ctx The context object containing all argument objects.
     * *
     * @throws SQLException Implementations are allowed to pass on all
     * *             [SQLException]s to the caller to be wrapped in
     * *             [DataAccessException]s.
     */
    override fun register(ctx: BindingRegisterContext<String>) {
        ctx.statement()?.registerOutParameter(ctx.index(), Types.VARCHAR)
    }
}