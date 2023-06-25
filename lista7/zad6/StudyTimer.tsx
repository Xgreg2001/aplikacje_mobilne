import React, { useState, useEffect } from 'react';
import { Text, View, Button, TextInput, StyleSheet, Alert } from 'react-native';
import * as Progress from 'react-native-progress';

const StudyTimer: React.FC = () => {
    const [time, setTime] = useState<number>(0);
    const [wasStarted, setWasStarted] = useState<boolean>(false);
    const [sessions, setSessions] = useState<number>(0);
    const [maxTime, setMaxTime] = useState<number>(0);
    const [input, setInput] = useState<string>('');
    const [isPaused, setIsPaused] = useState<boolean>(false);

    useEffect(() => {
        let interval: number | undefined;
        if (wasStarted && !isPaused) {
            interval = setInterval(() => {
                setTime(time => time - 1);
            }, 1000) as unknown as number;
        } else if (wasStarted && isPaused) {
            clearInterval(interval);
        }
        if (time <= 0 && wasStarted) {
            setIsPaused(false);
            setWasStarted(false);
            setSessions(sessions => sessions + 1);
            setTime(maxTime);
            Alert.alert("Study Timer", "Time's up!");
        }
        return () => clearInterval(interval);
    }, [wasStarted, isPaused, time]);

    const handleStart = () => {
        if (isNumber(input)) {
            setMaxTime(parseInt(input) * 60);
            setTime(maxTime);
            setWasStarted(true);
            setIsPaused(false);
        }
    };

    const handelPause = () => {
        setIsPaused(true);
    };

    const handleResume = () => {
        setIsPaused(false);
    };

    return (
        <View style={styles.container}>
            {Clock(time)}
            <Progress.Bar progress={(maxTime !== 0) ? (time / maxTime) : 0} style={styles.progress} />
            <Text style={styles.text}>Sessions: {sessions}</Text>
            <TextInput
                value={input}
                onChangeText={setInput}
                placeholder="Enter time in minutes"
                keyboardType="numeric"
                style={styles.textInput}
            />
            {ButtonGroup(wasStarted, isPaused, handleStart, handelPause, handleResume)}
        </View>
    );
};

function ButtonGroup(isActive: boolean, isPaused: boolean, handleStart: () => void, handelPause: () => void, handleResume: () => void) {
    if (isActive && !isPaused) {
        return <Button title="Pause" onPress={handelPause} />;
    }
    if (isActive && isPaused) {
        return <Button title="Resume" onPress={handleResume} />;
    }
    return <Button title="Start" onPress={handleStart} />;
}

function Clock(time: number) {
    if (time === null || time === undefined || isNaN(time)) {
        return <Text>Enter time</Text>;
    }
    if (time < 0) {
        return <Text>Time must be positive</Text>;
    }
    if (time === 0) {
        return <Text>Time's up!</Text>;
    }
    const minutes = Math.floor(time / 60);
    const seconds = time % 60;
    return (
        <Text style={styles.clock}>
            {minutes}:{seconds}
        </Text>
    );
}

function isNumber(n: string): boolean {
    return !isNaN(parseInt(n));
}

const styles = StyleSheet.create({
    container: {
        display: 'flex',
        flexDirection: 'column',
        backgroundColor: '#fff',
        alignItems: 'center',
        margin: 10,
    },
    text: {
        fontSize: 20,
    },
    textInput: {
        height: 40,
        width: 200,
        borderColor: 'gray',
        borderWidth: 1,
        margin: 10,
        padding: 5,
    },
    clock: {
        fontSize: 40,
    },
    progress: {
        margin: 10,
    },
});

export default StudyTimer;
