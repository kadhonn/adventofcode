use std::borrow::{Borrow, BorrowMut};
use std::cell::RefCell;
use std::collections::HashMap;
use std::ops::Deref;
use std::rc::Rc;

const FIELD_SIZE: i32 = 50;
const DIR_LEFT: (i32, i32) = (-1, 0);
const DIR_RIGHT: (i32, i32) = (1, 0);
const DIR_UP: (i32, i32) = (0, -1);
const DIR_DOWN: (i32, i32) = (0, 1);

pub fn day22_2(str: &str) {
    let (field, movements) = parse_input(str);

    let mut position = (0, 0);
    let mut direction = (1, 0);
    for x in 0..field[0].len() {
        if field[0][x] == '.' {
            position = (x as i32, 0);
            break;
        }
    }

    let transitions = build_transitions();

    let cube = RefCell::new(HashMap::new());
    build_cube(&cube, &field, &transitions, position, build_nothing(), 1);

    {
        let map = cube.borrow();
        for cube_sidenumber in map.keys() {
            let cube_side = &map[cube_sidenumber];
            let start_pos = cube_side.0;
            let transtioned_pos: (i32, i32) = cube_side.1.deref().1((0, 0));
            println!("cube side {} starts at {} {} and 0 0 is transitioned to {} {}",
            cube_sidenumber, start_pos.0, start_pos.1, transtioned_pos.0, transtioned_pos.1);
        }
    }

    let mut position = (1, position.0 % FIELD_SIZE, position.1 % FIELD_SIZE);
    for movement in movements {
        match movement {
            Movement::Left => { direction = turn_dir_left(direction); }
            Movement::Right => { direction = turn_dir_right(direction); }
            Movement::Steps(steps) => { move_steps(&field, &transitions, &cube, &mut position, &mut direction, steps); }
        }
    }

    let map = cube.borrow();
    let cube_side = map.get(&position.0).unwrap();
    let transitioned_pos: (i32, i32) = cube_side.1.deref().2.deref()((position.1, position.2));

    let result = 1000 * (transitioned_pos.1 as usize + cube_side.0.1 as usize + 1) + 4 * (transitioned_pos.0 as usize + cube_side.0.0 as usize + 1) + match direction {
        DIR_RIGHT => 0,
        DIR_DOWN => 1,
        DIR_LEFT => 2,
        DIR_UP => 3,
        _ => panic!("invalid direction! {:?}", direction)
    };

    println!("{}", result);
}

fn build_cube(cube: &RefCell<HashMap<i32, ((i32, i32), Transitions)>>, field: &Vec<Vec<char>>, transitions: &HashMap<i32, Side>, start_pos: (i32, i32), current_transitions: Transitions, side_number: i32) {
    let pos = start_pos.clone();

    if cube.borrow().contains_key(&side_number) {
        return;
    }
    if 0 <= pos.0 && pos.0 < field[0].len() as i32 && 0 <= pos.1 && pos.1 < field.len() as i32 && field[pos.1 as usize][pos.0 as usize] != ' ' {
        {
            let mut cube_mut = cube.borrow_mut();
            cube_mut.insert(side_number, (start_pos, current_transitions.clone()));
        }

        build_cube_dir(cube, field, transitions, current_transitions.clone(), side_number, pos, DIR_UP);
        build_cube_dir(cube, field, transitions, current_transitions.clone(), side_number, pos, DIR_DOWN);
        build_cube_dir(cube, field, transitions, current_transitions.clone(), side_number, pos, DIR_LEFT);
        build_cube_dir(cube, field, transitions, current_transitions.clone(), side_number, pos, DIR_RIGHT);
    }
}

fn build_cube_dir(cube: &RefCell<HashMap<i32, ((i32, i32), Transitions)>>, field: &Vec<Vec<char>>, transitions: &HashMap<i32, Side>, current_transitions: Transitions, side_number: i32, pos: (i32, i32), dir: (i32, i32)) {
    let pos = (pos.0 + dir.0 * FIELD_SIZE, pos.1 + dir.1 * FIELD_SIZE);

    let side_transition = transitions.get(&side_number).unwrap();
    let transitioned_dir: (i32, i32) = current_transitions.deref().0(dir);
    let side = side_transition.get(&transitioned_dir).unwrap();
    let old_transitions_1: Transitions = current_transitions.clone();
    let old_transitions_2: Transitions = current_transitions.clone();
    let old_transitions_3: Transitions = current_transitions.clone();
    let side_1 = side.1.clone();
    let side_2 = side.1.clone();
    let side_3 = side.1.clone();
    let new_transitions: Transitions = Rc::new((Box::new(move |a| { old_transitions_1.0(side_1.0(a)) }), Box::new(move |a| { old_transitions_2.1(side_2.1(a)) }), Box::new(move |a| { old_transitions_3.2(side_3.2(a)) })));
    build_cube(cube, field, transitions, pos, new_transitions, side.0);
}

fn move_steps(field: &Vec<Vec<char>>, transitions: &HashMap<i32, Side>, cube: &RefCell<HashMap<i32, ((i32, i32), Transitions)>>, position: &mut (i32, i32, i32), direction: &mut (i32, i32), steps: i32) {
    let mut new_pos = (position.0, position.1, position.2);
    let mut new_dir = (direction.0, direction.1);
    'outer: for _ in 0..steps {
        let mut pos = (new_pos.0, new_pos.1 + new_dir.0, new_pos.2 + new_dir.1);
        let mut dir = new_dir;
        if pos.1 < 0 || pos.1 >= FIELD_SIZE || pos.2 < 0 || pos.2 >= FIELD_SIZE {
            let old_side_number = pos.0;
            let side = transitions.get(&old_side_number).unwrap();
            let transition = side.get(&new_dir).unwrap();
            let transitioned_pos = ((pos.1 + FIELD_SIZE) % FIELD_SIZE, (pos.2 + FIELD_SIZE) % FIELD_SIZE);
            let transitioned_pos: (i32, i32) = transition.1.deref().1.deref()(transitioned_pos);
            pos = (transition.0, transitioned_pos.0, transitioned_pos.1);
            dir = transition.1.deref().0.deref()(dir);
            println!("transitioning from side {} to side {} meaning pos is from {} {} to {} {} and dir is from {} {} to {} {}",
                     old_side_number, pos.0, new_pos.1, new_pos.2, pos.1, pos.2, new_dir.0, new_dir.1, dir.0, dir.1);
        }
        if get_field(field, cube, pos) == '#' {
            break 'outer;
        }
        new_pos = pos;
        new_dir = dir;
    }
    position.0 = new_pos.0;
    position.1 = new_pos.1;
    position.2 = new_pos.2;
    direction.0 = new_dir.0;
    direction.1 = new_dir.1;
}

fn get_field(field: &Vec<Vec<char>>, cube: &RefCell<HashMap<i32, ((i32, i32), Transitions)>>, pos: (i32, i32, i32)) -> char {
    let map = cube.borrow();
    let cube_side = map.get(&pos.0).unwrap();
    let transitioned_pos: (i32, i32) = cube_side.1.deref().2.deref()((pos.1, pos.2));
    // println!("{} {} {} -> {} {}", pos.0, pos.1, pos.2, transitioned_pos.0, transitioned_pos.1);
    return field[transitioned_pos.1 as usize + cube_side.0.1 as usize][transitioned_pos.0 as usize + cube_side.0.0 as usize];
}

fn do_nothing(direction: (i32, i32)) -> (i32, i32) {
    return direction;
}

fn turn_dir_left(direction: (i32, i32)) -> (i32, i32) {
    return (direction.1, -direction.0);
}

fn turn_dir_right(direction: (i32, i32)) -> (i32, i32) {
    return (-direction.1, direction.0);
}

fn turn_dir_around(direction: (i32, i32)) -> (i32, i32) {
    return (-direction.0, -direction.1);
}

fn flip_pos_left(direction: (i32, i32)) -> (i32, i32) {
    return (direction.1, FIELD_SIZE - 1 - direction.0);
}

fn flip_pos_right(direction: (i32, i32)) -> (i32, i32) {
    return (FIELD_SIZE - 1 - direction.1, direction.0);
}

fn flip_pos_around(direction: (i32, i32)) -> (i32, i32) {
    return (FIELD_SIZE - 1 - direction.0, FIELD_SIZE - 1 - direction.1);
}

fn parse_input(str: &str) -> (Vec<Vec<char>>, Vec<Movement>) {
    let mut lines = str.lines();

    let mut field: Vec<Vec<char>> = vec![];

    loop {
        let line = lines.next().unwrap();
        if line.is_empty() {
            break;
        }
        field.push(line.chars().collect())
    }

    let mut max_cols = 0;
    for line in &field {
        max_cols = max_cols.max(line.len())
    }

    for line in &mut field {
        for _ in 0..(max_cols - line.len()) {
            line.push(' ');
        }
    }

    let mut movements = vec![];
    let chars = lines.next().unwrap().chars();
    let mut buf = String::new();
    for char in chars {
        if char.is_digit(10) {
            buf.push(char);
        } else {
            if !buf.is_empty() {
                movements.push(Movement::Steps(buf.parse().unwrap()));
                buf.clear();
            }
            match char {
                'L' => movements.push(Movement::Left),
                'R' => movements.push(Movement::Right),
                _ => { panic!("invalid char {}", char); }
            }
        }
    }
    if !buf.is_empty() {
        movements.push(Movement::Steps(buf.parse().unwrap()));
        buf.clear();
    }

    (field, movements)
}

#[derive(Debug)]
enum Movement {
    Steps(i32),
    Left,
    Right,
}

fn build_transitions() -> HashMap<i32, Side> {
    let mut transitions: HashMap<i32, Side> = HashMap::new();

    let NOTHING: Transitions = build_nothing();
    let LEFT: Transitions = build_left();
    let RIGHT: Transitions = build_right();
    let AROUND: Transitions = build_around();


    transitions.insert(0, HashMap::from([
        (DIR_LEFT, (2, NOTHING.clone())),
        (DIR_RIGHT, (4, AROUND.clone())),
        (DIR_UP, (1, LEFT.clone())),
        (DIR_DOWN, (3, RIGHT.clone())),
    ]));
    transitions.insert(1, HashMap::from([
        (DIR_LEFT, (5, LEFT.clone())),
        (DIR_RIGHT, (0, RIGHT.clone())),
        (DIR_UP, (4, NOTHING.clone())),
        (DIR_DOWN, (2, NOTHING.clone())),
    ]));
    transitions.insert(2, HashMap::from([
        (DIR_LEFT, (5, NOTHING.clone())),
        (DIR_RIGHT, (0, NOTHING.clone())),
        (DIR_UP, (1, NOTHING.clone())),
        (DIR_DOWN, (3, NOTHING.clone())),
    ]));
    transitions.insert(3, HashMap::from([
        (DIR_LEFT, (5, RIGHT.clone())),
        (DIR_RIGHT, (0, LEFT.clone())),
        (DIR_UP, (2, NOTHING.clone())),
        (DIR_DOWN, (4, NOTHING.clone())),
    ]));
    transitions.insert(4, HashMap::from([
        (DIR_LEFT, (5, AROUND.clone())),
        (DIR_RIGHT, (0, AROUND.clone())),
        (DIR_UP, (3, NOTHING.clone())),
        (DIR_DOWN, (1, NOTHING.clone())),
    ]));
    transitions.insert(5, HashMap::from([
        (DIR_LEFT, (4, AROUND.clone())),
        (DIR_RIGHT, (2, NOTHING.clone())),
        (DIR_UP, (1, RIGHT.clone())),
        (DIR_DOWN, (3, LEFT.clone())),
    ]));

    transitions
}

fn build_around() -> Transitions {
    Rc::new((Box::new(turn_dir_around), Box::new(flip_pos_around), Box::new(flip_pos_around)))
}

fn build_right() -> Transitions {
    Rc::new((Box::new(turn_dir_right), Box::new(flip_pos_right), Box::new(flip_pos_left)))
}

fn build_left() -> Transitions {
    Rc::new((Box::new(turn_dir_left), Box::new(flip_pos_left), Box::new(flip_pos_right)))
}

fn build_nothing() -> Transitions {
    Rc::new((Box::new(do_nothing), Box::new(do_nothing), Box::new(do_nothing)))
}

type Transition = dyn Fn((i32, i32)) -> (i32, i32);
type Transitions = Rc<(Box<Transition>, Box<Transition>, Box<Transition>)>;
type Side = HashMap<(i32, i32), (i32, Transitions)>;
