drop table if exists scenarios, sensors, conditions, actions, scenario_conditions, scenario_actions;
-- создаём таблицу scenarios
CREATE TABLE IF NOT EXISTS scenarios (
    id          BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hub_id      VARCHAR     NOT NULL,
    name        VARCHAR     NOT NULL,
    UNIQUE(hub_id, name)
);

-- создаём таблицу sensors
CREATE TABLE IF NOT EXISTS sensors (
    id      VARCHAR PRIMARY KEY,
    hub_id  VARCHAR NOT NULL,
    type    VARCHAR NOT NULL
);

-- создаём таблицу conditions
CREATE TABLE IF NOT EXISTS conditions (
    id          BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type        VARCHAR     NOT NULL,
    operation   VARCHAR     NOT NULL,
    amount       INTEGER     NOT NULL
);

-- создаём таблицу actions
CREATE TABLE IF NOT EXISTS actions (
    id          BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type        VARCHAR     NOT NULL,
    amount      INTEGER     NOT NULL
);

-- создаём таблицу scenario_conditions, связывающую сценарий, датчик и условие активации сценария
CREATE TABLE IF NOT EXISTS scenario_conditions (
    scenario_id     BIGINT      REFERENCES scenarios(id),
    sensor_id       VARCHAR     REFERENCES sensors(id),
    condition_id    BIGINT      REFERENCES conditions(id),
    PRIMARY KEY (scenario_id, sensor_id, condition_id)
);

-- создаём таблицу scenario_actions, связывающую сценарий, датчик и действие, которое нужно выполнить при активации сценария
CREATE TABLE IF NOT EXISTS scenario_actions (
    scenario_id BIGINT  REFERENCES scenarios(id),
    sensor_id VARCHAR   REFERENCES sensors(id),
    action_id BIGINT    REFERENCES actions(id),
    PRIMARY KEY (scenario_id, sensor_id, action_id)
);