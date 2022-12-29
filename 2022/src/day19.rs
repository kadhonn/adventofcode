use std::cmp::max;
use std::collections::HashSet;
use std::hash::{Hash, Hasher};
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
    for i in 0..(3.max(blueprints.len())) {
        let blueprint = blueprints[i];
        let best_result = get_best(blueprint);
        prod *= best_result;
        println!("result: {}", best_result);
    }

    println!("{}", prod);
}

fn get_best(blueprint: Blueprint) -> i32 {
    let mut current_states = vec![State::new()];

    for round in 0..ROUNDS {
        println!("{}: {}", round, current_states.len());
        let mut next_states = vec![];
        for state in current_states {
            for next_state in get_next_states(&blueprint, state) {
                next_states.push(next_state);
            }
        }
        current_states = prune(round, next_states);
    }

    let mut max = 0;
    for state in current_states {
        max = max.max(state.geodes);
    }

    max
}

fn prune(round: i32, states: Vec<State>) -> Vec<State> {
    let rounds_till_end = ROUNDS - round - 1;
    let max_geodes_till_end: i32 = (1..rounds_till_end).sum();

    let mut max_cur = 0;
    for state in states.iter() {
        max_cur = max_cur.max(state.geodes);
    }

    let mut next_states = vec![];
    let mut best = states[0];
    for state in states {
        if state.geodes + state.geode_robots * rounds_till_end + max_geodes_till_end >= max_cur {
            next_states.push(state);

            if state.total_ore > best.total_ore
                && state.total_clay > best.total_clay
                && state.total_obsidian > best.total_obsidian
                && state.total_geodes > best.total_geodes
                && state.ore_robots > best.ore_robots
                && state.clay_robots > best.clay_robots
                && state.obsidian_robots > best.obsidian_robots
                && state.geode_robots > best.geode_robots {
                best = state.clone();
            }
        }
    }

    let mut bests: Vec<State> = vec![];
    'outer: for state in next_states.into_iter() {
        if state.total_ore < best.total_ore
            && state.total_clay < best.total_clay
            && state.total_obsidian < best.total_obsidian
            && state.total_geodes < best.total_geodes
            && state.ore_robots < best.ore_robots
            && state.clay_robots < best.clay_robots
            && state.obsidian_robots < best.obsidian_robots
            && state.geode_robots < best.geode_robots {
            continue 'outer;
        }
        bests.push(state);
    }

    let hashed: HashSet<State> = HashSet::from_iter(bests.into_iter());
    hashed.into_iter().collect()
}

fn get_next_states(blueprint: &Blueprint, state: State) -> Vec<State> {
    let mut next_states = vec![];

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
    state.total_ore += state.ore_robots;
    state.total_clay += state.clay_robots;
    state.total_obsidian += state.obsidian_robots;
    state.total_geodes += state.geode_robots;
    state
}

#[derive(Copy, Clone)]
struct State {
    total_ore: i32,
    total_clay: i32,
    total_obsidian: i32,
    total_geodes: i32,
    ore: i32,
    clay: i32,
    obsidian: i32,
    geodes: i32,
    ore_robots: i32,
    clay_robots: i32,
    obsidian_robots: i32,
    geode_robots: i32,
}

impl PartialEq<Self> for State {
    fn eq(&self, other: &Self) -> bool {
        return self.ore_robots == other.ore_robots
            && self.clay_robots == other.clay_robots
            && self.obsidian_robots == other.obsidian_robots
            && self.geode_robots == other.geode_robots
            && self.total_ore == other.total_ore
            && self.total_clay == other.total_clay
            && self.total_obsidian == other.total_obsidian
            && self.total_geodes == other.total_geodes;
    }
}

impl Eq for State {}

impl Hash for State {
    fn hash<H: Hasher>(&self, state: &mut H) {
        self.ore_robots.hash(state);
        self.clay_robots.hash(state);
        self.obsidian_robots.hash(state);
        self.geode_robots.hash(state);
        self.total_ore.hash(state);
        self.total_clay.hash(state);
        self.total_obsidian.hash(state);
        self.total_geodes.hash(state);
    }
}

impl State {
    fn new() -> State {
        State {
            ore: 0,
            clay: 0,
            obsidian: 0,
            geodes: 0,
            ore_robots: 1,
            clay_robots: 0,
            obsidian_robots: 0,
            geode_robots: 0,
            total_ore: 0,
            total_clay: 0,
            total_obsidian: 0,
            total_geodes: 0,
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