use std::hash::{Hash};
use priority_queue::PriorityQueue;
use regex::Regex;

const ROUNDS: i32 = 32;

pub fn day19_1(str: &String) {
    let blueprints = parse_blueprints(str);

    let mut sum = 0;
    for blueprint in blueprints {
        let best_result = get_best(blueprint);
        sum += blueprint.0 * best_result;
        println!("{} * {} = {}", best_result, blueprint.0, blueprint.0 * best_result);
    }

    println!("{}", sum);
}

pub fn day19_2(str: &String) {
    let blueprints = parse_blueprints(str);

    let mut prod = 1;
    for i in 0..(3.min(blueprints.len())) {
        let blueprint = blueprints[i];
        let best_result = get_best(blueprint);
        prod *= best_result;
        println!("blueprint {}: {}", blueprint.0, best_result);
    }

    println!("{}", prod);
}

fn get_best(blueprint: Blueprint) -> i32 {
    let mut current_states = PriorityQueue::new();
    current_states.push(State::new(), get_priority(&State::new()));

    loop {
        let current_state = current_states.pop().unwrap().0;
        if current_state.round == ROUNDS {
            return current_state.geodes;
        }

        let next_states = get_next_states(&blueprint, current_state);
        for next_state in next_states {
            current_states.push(next_state, get_priority(&next_state));
        }
    }
}

fn get_priority(state: &State) -> i32 {
    let round = state.round;
    let rounds_till_end = ROUNDS - round;
    let max_geodes_till_end: i32 = (1..rounds_till_end).sum();
    state.geodes + state.geode_robots * rounds_till_end + max_geodes_till_end
}

fn get_next_states(blueprint: &Blueprint, state: State) -> Vec<State> {
    let mut next_states = vec![];
    let mut state = state;
    state.round += 1;

    //do not buy anything
    next_states.push(collect_ore(state));

    //buy ore robot
    if state.ore >= blueprint.1 {
        let mut state = state;
        state.ore -= blueprint.1;
        state = collect_ore(state);
        state.ore_robots += 1;
        next_states.push(state);
    }

    //buy clay robot
    if state.ore >= blueprint.2 {
        let mut state = state;
        state.ore -= blueprint.2;
        state = collect_ore(state);
        state.clay_robots += 1;
        next_states.push(state);
    }

    //buy obsidian robot
    if state.ore >= blueprint.3.0 && state.clay >= blueprint.3.1 {
        let mut state = state;
        state.ore -= blueprint.3.0;
        state.clay -= blueprint.3.1;
        state = collect_ore(state);
        state.obsidian_robots += 1;
        next_states.push(state);
    }

    //buy geode robot
    if state.ore >= blueprint.4.0 && state.obsidian >= blueprint.4.1 {
        let mut state = state;
        state.ore -= blueprint.4.0;
        state.obsidian -= blueprint.4.1;
        state = collect_ore(state);
        state.geode_robots += 1;
        next_states.push(state);
    }

    next_states
}

fn collect_ore(state: State) -> State {
    let mut state = state;
    state.ore += state.ore_robots;
    state.clay += state.clay_robots;
    state.obsidian += state.obsidian_robots;
    state.geodes += state.geode_robots;
    state
}

#[derive(Copy, Clone, Eq, PartialEq, Hash)]
struct State {
    round: i32,
    ore: i32,
    clay: i32,
    obsidian: i32,
    geodes: i32,
    ore_robots: i32,
    clay_robots: i32,
    obsidian_robots: i32,
    geode_robots: i32,
}

impl State {
    fn new() -> State {
        State {
            round: 0,
            ore: 0,
            clay: 0,
            obsidian: 0,
            geodes: 0,
            ore_robots: 1,
            clay_robots: 0,
            obsidian_robots: 0,
            geode_robots: 0,
        }
    }
}

fn parse_blueprints(str: &String) -> Vec<Blueprint> {
    let regex = Regex::new(r"Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.").unwrap();

    let mut blueprints = vec![];

    for line in str.trim().lines() {
        let blueprint = regex.captures_iter(line).next().unwrap();
        blueprints.push((
            blueprint[1].parse().unwrap(),
            blueprint[2].parse().unwrap(),
            blueprint[3].parse().unwrap(),
            (blueprint[4].parse().unwrap(), blueprint[5].parse().unwrap()),
            (blueprint[6].parse().unwrap(), blueprint[7].parse().unwrap()),
        ));
    }

    blueprints
}

type Ore = i32;
type Clay = i32;
type Obsidian = i32;
type Geode = i32;

type Blueprint = (i32, Ore, Ore, (Ore, Clay), (Ore, Obsidian));