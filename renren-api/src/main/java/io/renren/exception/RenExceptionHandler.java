 

package io.renren.exception;

import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 异常处理器
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@RestControllerAdvice
public class RenExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(RenExceptionHandler.class);

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RenException.class)
	public Result handleRenException(RenException ex){
		logger.error("Custom exception: {}", ex.getMessage(), ex);
		Result result = new Result();
		result.error(ex.getCode(), ex.getMsg());
		return result;
	}

	/**
	 * 处理数据库重复键异常
	 */
	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Result handleDuplicateKeyException(DuplicateKeyException ex){
		logger.error("Duplicate key exception: {}", ex.getMessage(), ex);
		Result result = new Result();
		result.error(ErrorCode.DB_RECORD_EXISTS);
		return result;
	}

	/**
	 * 处理参数校验异常
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		logger.error("Method argument not valid: {}", ex.getMessage(), ex);
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
			.map(FieldError::getDefaultMessage)
			.collect(Collectors.joining(", "));
		return new Result().error(ErrorCode.NOT_NULL, errorMessage);
	}

	/**
	 * 处理绑定异常
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result handleBindException(BindException ex){
		logger.error("Bind exception: {}", ex.getMessage(), ex);
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
			.map(FieldError::getDefaultMessage)
			.collect(Collectors.joining(", "));
		return new Result().error(ErrorCode.NOT_NULL, errorMessage);
	}

	/**
	 * 处理约束违反异常
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result handleConstraintViolationException(ConstraintViolationException ex){
		logger.error("Constraint violation: {}", ex.getMessage(), ex);
		String errorMessage = ex.getConstraintViolations().stream()
			.map(ConstraintViolation::getMessage)
			.collect(Collectors.joining(", "));
		return new Result().error(ErrorCode.NOT_NULL, errorMessage);
	}

	/**
	 * 处理通用异常
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result handleException(Exception ex){
		logger.error("Unexpected exception: {}", ex.getMessage(), ex);
		return new Result().error();
	}
}