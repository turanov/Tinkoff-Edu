package ru.tinkoff.fintech.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import ru.tinkoff.fintech.model.Course;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseTypeHandler extends BaseTypeHandler<Course> {
    private ObjectMapper objectMapper;

    public CourseTypeHandler() {
        this.objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Course course, JdbcType jdbcType
    ) throws SQLException {
        preparedStatement.setString(i, objectMapper.writeValueAsString(course));
    }

    @SneakyThrows
    @Override
    public Course getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String course = resultSet.getObject(s, String.class);
        return objectMapper.readValue(course, Course.class);
    }

    @SneakyThrows
    @Override
    public Course getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String course = resultSet.getObject(i, String.class);
        return objectMapper.readValue(course, Course.class);
    }

    @SneakyThrows
    @Override
    public Course getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String course = callableStatement.getObject(i, String.class);
        return objectMapper.readValue(course, Course.class);
    }
}
