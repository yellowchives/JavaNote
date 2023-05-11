/**
 * 无关联的资源使用不同的锁
 * 细粒度锁能够提升性能
 */
public class SomeLocks05 {

    // 锁：保护账户余额
    private final Object balanceLock = new Object();
    private int balance;

    // 锁：保护账户密码
    private final Object passwordLock = new Object();
    private String password;

    //取款
    public void transfer(int amt) {
        synchronized (balanceLock) {
            if (this.balance > amt) {
                this.balance -= amt;
            }
        }
    }
    // 查看余额
    Integer getBalance() {
        synchronized(balanceLock) {
            return balance;
        }
    }

    // 更改密码
    void updatePassword(String pw){
        synchronized(passwordLock) {
            this.password = pw;
        }
    }
    // 查看密码
    String getPassword() {
        synchronized(passwordLock) {
            return password;
        }
    }
}
