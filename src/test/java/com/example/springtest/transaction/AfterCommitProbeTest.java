package com.example.springtest.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

class AfterCommitProbeTest {

	private final AfterCommitProbe probe = new AfterCommitProbe();
	private final TransactionTemplate transactionTemplate = new TransactionTemplate(new StubTransactionManager());

	@Test
	void afterCommitIsInvokedAfterCommitSequence() {
		List<String> events = new ArrayList<>();

		transactionTemplate.executeWithoutResult(status -> {
			events.add("inTransaction");
			probe.registerCallbacks(events);
			events.add("transactionBodyEnd");
		});

		assertThat(events).containsExactly(
			"inTransaction",
			"register",
			"registered",
			"transactionBodyEnd",
			"beforeCommit",
			"beforeCompletion",
			"afterCommit",
			"afterCompletion:COMMITTED"
		);
	}

	@Test
	void afterCommitIsNotInvokedOnRollback() {
		List<String> events = new ArrayList<>();

		transactionTemplate.executeWithoutResult(status -> {
			probe.registerCallbacks(events);
			events.add("markRollbackOnly");
			status.setRollbackOnly();
		});

		assertThat(events).containsExactly(
			"register",
			"registered",
			"markRollbackOnly",
			"beforeCompletion",
			"afterCompletion:ROLLED_BACK"
		);
	}

	@Test
	void registeringWithoutActiveTransactionFails() {
		assertThat(TransactionSynchronizationManager.isSynchronizationActive()).isFalse();

		assertThatThrownBy(() -> probe.registerCallbacks(new ArrayList<>()))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("Transaction synchronization is not active");
		}

	private static class StubTransactionManager extends AbstractPlatformTransactionManager {

		@Override
		protected Object doGetTransaction() {
			return new Object();
		}

		@Override
		protected void doBegin(Object transaction, TransactionDefinition definition) {
		}

		@Override
		protected void doCommit(DefaultTransactionStatus status) {
		}

		@Override
		protected void doRollback(DefaultTransactionStatus status) {
		}
	}
}