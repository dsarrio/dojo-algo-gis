module.exports = {
    transform: {'^.+\\.ts?$': 'ts-jest'},
    testEnvironment: 'node',
    testRegex: '/src/.*\\.(test|spec)?\\.(ts|tsx)$',
    moduleFileExtensions: ['ts', 'tsx', 'js', 'jsx', 'json', 'node'],
    collectCoverage: true,
    collectCoverageFrom: [
      'src/**/*.ts',
      '!src/solution.ts',
      '!src/core/actions.ts',
      '!src/**/*.d.ts',
      '!src/**/*.test.ts',
    ],
    coverageReporters: ['lcov', 'text', 'text-summary']
  };