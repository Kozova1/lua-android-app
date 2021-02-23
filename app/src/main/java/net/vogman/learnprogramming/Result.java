package net.vogman.learnprogramming;

public class Result<T, E> {
  private enum Type {
    RESULT_OK,
    RESULT_ERR
  }
  private T ok;
  private E err;
  private Type tag;

  public Result() {}

  public Result<T, E> ok(T value) {
    this.ok = value;
    this.tag = Type.RESULT_OK;
    this.err = null;
    return this;
  }

  public Result<T, E> err(E value) {
    this.err = value;
    this.tag = Type.RESULT_ERR;
    this.ok = null;
    return this;
  }

  public boolean isOk() {
    return this.tag == Type.RESULT_OK;
  }

  public boolean isErr() {
    return this.tag == Type.RESULT_ERR;
  }

  public T unwrapOr(T otherwise) {
    return this.tag == Type.RESULT_OK ? this.ok : otherwise;
  }

  public T unwrap() {
    if (this.tag == Type.RESULT_ERR)
      return null;
    return this.ok;
  }
}
